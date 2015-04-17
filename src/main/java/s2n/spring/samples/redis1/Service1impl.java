package s2n.spring.samples.redis1;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("helloService")
public class Service1impl implements HelloService {

	@Cacheable(value = AppConfig.CAC)
	public String getMessage(String name) {
		System.out.println("*** In ServiceImpl" + ".getHelloMessage(\"" + name + "\")");
		return "Hello " + name + "!";
	}

}