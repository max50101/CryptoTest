package com.example.scanner_api;
import com.example.scanner_api.MarketScanOptionsDto;
import com.example.scanner_api.IMarketScanCallback;

interface IMarketScanService{
    long startScan(in MarketScanOptionsDto options, in IMarketScanCallback callback);
    void cancelScan(long taskId);
    boolean isTaskRunning(long taskId);
}