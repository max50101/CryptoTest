package com.example.data.coins.impl

import android.util.Log
import com.example.data.coins.api.websocket.BinanceWebSocket
import com.example.data.coins.helpers.toCoin
import com.example.data.coins.model.toBinanceKlineDto
import com.example.data.coins.model.toCoinEntity
import com.example.data.coins.model.toCoinKline
import com.example.database.features.CoinsDao
import com.example.datastore.CoinsListSync
import com.example.domain.coins.repository.CoinsRepository
import com.example.model.Coin
import com.example.model.CoinKline
import com.example.network.api.CoinsApi
import com.example.network.safeApiCall
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CoinsRepositoryImp @Inject constructor(
    private val coinsApi: CoinsApi,
    private val binanceWebSocket: BinanceWebSocket,
    private val coinsDao: CoinsDao,
    private val coinsListSync: CoinsListSync
) : CoinsRepository {

    override fun observeFavoriteCoins(): Flow<List<Coin>> {
        return coinsDao.observeFavoriteCoins()
            .map { entities -> entities.map { it -> it.toCoin() } }
            .flatMapLatest { coinsFromDb ->
                if (coinsFromDb.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    val symbols = coinsFromDb.map { coin ->
                        coin.symbol
                    }
                    binanceWebSocket.observePrices(symbols)
                        .runningFold(coinsFromDb) { currentCoins, priceUpdated ->
                            currentCoins.map { coin ->
                                if (coin.symbol == priceUpdated.symbol) {
                                    coin.copy(priceUsd = priceUpdated.price)
                                } else {
                                    coin
                                }
                            }

                        }
                }

            }
    }

    override fun observeCoinChart(symbol: String, interval: String): Flow<List<CoinKline>> = flow {
        val klines = safeApiCall { coinsApi.getUiKlines(symbol, interval) }.map {
            it.toBinanceKlineDto(symbol)
        }.map {
            it.toCoinKline(interval)
        }
        val history = klines.toMutableList()
        emit(history.toList())
        binanceWebSocket.observerCoinDetails(symbol, interval).collect { liveCline ->
            Log.i("observeCoinChart", "Enter parse")
            val kline = liveCline.toCoinKline(interval)
            val lastIndex = history.indexOfLast {
                it.openTime == kline.openTime
            }
            if (lastIndex != -1) {
                history[lastIndex] = kline
            } else {
                history.add(kline)
            }
            if (history.size > 500) {
                history.removeAt(0)
            }

            emit(history.toList())
        }

    }

    override fun observeCoins(): Flow<List<Coin>> {
        return coinsDao.observeCoins().map { it -> it.map { it.toCoin() } }

    }

    override fun observeCoinDetails(symbol: String): Flow<Coin> {
        return coinsDao.observeCoin(symbol).map { it.toCoin() }
    }

    override fun observeCoinPrice(symbol: String): Flow<Coin> {
        return coinsDao.observeCoin(symbol).flatMapLatest { coinFromDb ->
            if (coinFromDb==null) {
                flowOf(Coin())
            } else {
                binanceWebSocket.observePrices(listOf(coinFromDb.symbol)).map { priceUpdated ->
                    coinFromDb
                        .toCoin()
                        .copy(priceUsd = priceUpdated.price)
                }
            }
        }

    }

    override suspend fun refreshCoins() {
        if (!coinsListSync.shallUpdateCoins()) {
            return
        }
        val coins = safeApiCall { coinsApi.getCoinsList().symbols }
        coinsDao.updateCoinsKeepingFavorite(coins.map { it.toCoinEntity() })
        coinsListSync.setUpdateTime()
    }

    override suspend fun toggleFavorite(symbol: String) {
        coinsDao.updateCoin(symbol)
    }

}