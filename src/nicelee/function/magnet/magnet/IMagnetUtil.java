package nicelee.function.magnet.magnet;

import java.util.List;

import nicelee.function.magnet.domain.Magnet;

public interface IMagnetUtil {

	
	List<Magnet> getMagnetsById(String avId);
	
	List<Magnet> getMagnetsByUrl(String url);
}
