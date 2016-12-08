import grails.util.GrailsUtil
import grails.util.Holders


class AuthorizeNetGrailsPlugin {
    // the plugin version
    def version = "0.13"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4.4 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

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

    private ConfigObject loadAuthorizeNetConfig() {
        def config = Holders.config
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
