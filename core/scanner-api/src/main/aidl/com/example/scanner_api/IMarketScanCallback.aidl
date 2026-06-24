package com.example.scanner_api;
import com.example.scanner_api.MarketSignalDto;
interface IMarketScanCallback{
     void onStarted(long taskId);

        void onProgress(
            long taskId,
            int processed,
            int total,
            String currentSymbol
        );

        void onSignalFound(long taskId, in MarketSignalDto signal);

        void onCompleted(long taskId, in List<MarketSignalDto> signals);

        void onError(long taskId, String message);

        void onCancelled(long taskId);
}