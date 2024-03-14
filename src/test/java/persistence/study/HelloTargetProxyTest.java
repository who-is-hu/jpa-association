package persistence.study;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.Test;
import persistence.Person;

import java.lang.reflect.Method;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CapitalizeInterceptor implements MethodInterceptor {
    private final Object target;

    public CapitalizeInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = proxy.invoke(target, args);
        return ((String)result).toUpperCase();
    }
}
public class HelloTargetProxyTest {
    @Test
    void testUpperCaseProxy(){
        HelloTarget helloTarget = new HelloTarget();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloTarget.class);
        enhancer.setCallback(new CapitalizeInterceptor(helloTarget));
        HelloTarget proxy = (HelloTarget) enhancer.create();


        assertSoftly(softly -> {
            softly.assertThat(proxy.sayHello("yonghu")).isEqualTo("HELLO YONGHU");
            softly.assertThat(proxy.sayHi("yonghu")).isEqualTo("HI YONGHU");
            softly.assertThat(proxy.sayThankYou("yonghu")).isEqualTo("THANK YOU YONGHU");
        });
    }
}
