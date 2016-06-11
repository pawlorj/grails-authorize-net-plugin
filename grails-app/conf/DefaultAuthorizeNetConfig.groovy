authorizeNet {
    testMode = false
    duplicateWindow = '120'  //Default is 120 (2 minutes)
    delimiter = ';'
}

environments {
    development {
        authorizeNet {
            login = '<--PUT LOGIN ID HERE-->'
            transactionKey = '<--PUT TRANSACTION KEY HERE-->'
            urlString = 'https://test.authorize.net/gateway/transact.dll'
        }
    }
}

