import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.util.GrailsUtil


class AuthorizeNetGrailsPlugin {
    // the plugin version
    def version = "0.12"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Bob Pawlowski"
    def authorEmail = "bob@vinomis.com"
    def title = "Authorize.net Plugin"
    def description = '''\\
Authorize.net Plugin does simple authorize/capture, void, and refund operations.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/authorize-net"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        loadAuthorizeNetConfig()
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
    
    private ConfigObject loadAuthorizeNetConfig() {
        def config = ConfigurationHolder.config
        GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)

        // merging default Authorize.net config into main application config
        config.merge(new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('DefaultAuthorizeNetConfig')))

        // merging user-defined Quartz config into main application config if provided
        try {
            config.merge(new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('AuthorizeNetConfig')))
        } catch (Exception ignored) {
            // ignore, just use the defaults
        }

        return config.authorizeNet
    }
}
