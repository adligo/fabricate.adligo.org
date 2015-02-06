package org.adligo.fabricate.common.files;

import org.adligo.fabricate.common.log.I_FabLogSystem;
import org.apache.http.impl.client.CloseableHttpClient;

public interface I_FabFilesSystem extends I_FabLogSystem {
  public CloseableHttpClient newHttpClient ();
  
}
