import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class BeanLogger(@Autowired private val context: ApplicationContext) {

    init {
        val beanNames = context.beanDefinitionNames
        beanNames.sort()
        println("==== 등록된 빈 목록 ====")
        beanNames.forEach { println(it) }
    }
}
