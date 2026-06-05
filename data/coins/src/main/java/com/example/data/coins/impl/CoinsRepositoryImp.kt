package com.example.data.coins.impl

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.data.coins.api.CoinsApi
import com.example.data.coins.api.websocket.BinanceWebSocket
import com.example.data.coins.helpers.toBinanceSymbol
import com.example.data.coins.helpers.toCoin
import com.example.database.features.CoinsDao
import com.example.domain.coins.repository.CoinsRepository
import com.example.model.Coin
import com.example.network.safeApiCall
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CoinsRepositoryImp @Inject constructor(val coinsApi: CoinsApi, val binanceWebSocket: BinanceWebSocket, val coinsDao: CoinsDao): CoinsRepository {

    override fun observeFavoriteCoins(): Flow<List<Coin>> {
      return  coinsDao.observeCoins()
          .map { entities -> entities.map { it->it.toCoin() } }
          .flatMapLatest { coinsFromDb->
              if(coinsFromDb.isEmpty()){
                  flowOf(emptyList())
              }else{
                  val symbols=coinsFromDb.map{coin->
                        coin.toBinanceSymbol()
                  }
                  binanceWebSocket.observePrices(symbols)
                      .runningFold(coinsFromDb){currentCoins, priceUpdated->
                          currentCoins.map{coin->
                              if(coin.toBinanceSymbol()==priceUpdated.symbol){
                                  coin.copy(priceUsd = priceUpdated.price)
                              }else{
                                  coin
                              }
                          }

                      }
              }

          }
    }

    override fun observeCoinDetails(id: String): Flow<Coin> {
        TODO("Not yet implemented")
    }

    override fun observeCoins(): Flow<List<Coin>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshCoins() {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(id: String) {
        TODO("Not yet implemented")
    }

}