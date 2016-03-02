package h3.com.happyhog.Dropbox;

import h3.com.happyhog.Animal; /**
 * Created by sloth on 2016-03-02.
 */
public interface Serializable {
  boolean deserialize(String json) ;
  String serialize();
}