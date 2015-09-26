/*
 * Copyright 2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugins.spring.mobile

import grails.plugins.Plugin
import groovy.util.logging.Commons
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor
import org.springframework.mobile.device.LiteDeviceResolver

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 * @author <a href='mailto:scm.blanc@gmail.com'>Sebastien Blanc</a>
 * @author <a href='mailto:alexey@zhokhov.com'>Alexey Zhokhov</a>
 */
@Commons
class SpringMobileGrailsPlugin extends Plugin {

    def grailsVersion = '3.0.0 > *'

    def title = 'Spring Mobile'
    def author = 'Alexey Zhokhov'
    def authorEmail = 'donbeave@gmail.com'
    def description = 'Device resolver based on the Spring Mobile Library'

    def documentation = 'https://github.com/donbeave/grails-spring-mobile'

    def license = 'APACHE'

    def organization = [name: 'AZ', url: 'http://www.zhokhov.com']

    def developers = [
            [name: 'Burt Beckwith', email: 'burt@burtbeckwith.com'],
            [name: 'Sebastien Blanc', email: 'scm.blanc@gmail.com'],
            [name: 'Alexey Zhokhov', email: 'alexey@zhokhov.com']
    ]

    def issueManagement = [system: 'github', url: 'https://github.com/donbeave/grails-spring-mobile/issues']
    def scm = [url: 'https://github.com/donbeave/grails-spring-mobile']

    def observe = ['controllers']

    Closure doWithSpring() {
        { ->
            springMobileDeviceResolver(LiteDeviceResolver)
            springMobileDeviceResolverHandlerInterceptor(DeviceResolverHandlerInterceptor, ref('springMobileDeviceResolver'))
        }
    }

    void doWithDynamicMethods() {
        for (cc in grailsApplication.controllerClasses) {
            addDynamicMethods cc.clazz
        }
    }

    void onChange(Map<String, Object> event) {
        doWithDynamicMethods()
    }

    private void addDynamicMethods(klass) {
        klass.metaClass.withMobileDevice = { Closure closure ->
            def device = request.currentDevice
            if (device?.isMobile()) {
                closure.call device
            }
        }

        klass.metaClass.withTableDevice = { Closure closure ->
            def device = request.currentDevice
            if (device?.isTablet()) {
                closure.call device
            }
        }

        klass.metaClass.withNormalDevice = { Closure closure ->
            def device = request.currentDevice
            if (device?.isNormal()) {
                closure.call device
            }
        }

        klass.metaClass.isMobile = { -> request.currentDevice.isMobile() }

        klass.metaClass.isTablet = { -> request.currentDevice.isTablet() }

        klass.metaClass.isNormal = { -> request.currentDevice.isNormal() }
    }

}
