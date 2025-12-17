package de.hr.dmx4.sportservice.sportsaisonnotifier.controller.service;

import java.io.InputStream;

public interface FileStorageServiceClientIF {


    public void uploadToGCP(InputStream file);
    public InputStream loadLatest();






}
