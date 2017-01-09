package com.vinomis.authnet

import grails.util.Holders

/**
 *
 * @author Bob Pawlowski
 */
class AuthorizeNet extends BuilderSupport {

    def writer

    def config = Holders.config

    String login = config?.authorizeNet?.login
    String transactionKey = config?.authorizeNet?.transactionKey
    String urlString = config?.authorizeNet?.urlString
    boolean testMode = config?.authorizeNet?.testMode
    String duplicateWindow = config?.authorizeNet?.duplicateWindow  //Default is 120 (2 minutes)
    String delimiter = config?.authorizeNet?.delimiter
    def connection
    int nodeCount = 0

    def createWriter() {
        if (true) {
            def url = new URL(urlString)
            connection = url.openConnection()
            connection.setRequestMethod("POST")
            connection.doOutput = true
            connection.doInput = true
            return new OutputStreamWriter(connection.outputStream)
        } else {
            return new OutputStreamWriter(System.out)
        }
    }

    def submit() {
        writer.flush()
        if (true) {
            writer.close()
            connection.connect()
            def response = connection.content.text
            def anr = new AuthorizeNetResponse(response)
            return anr
        }

    }


    def createNode(name) {
        if (name.toLowerCase() == 'item') {
            itemMode = true
            return
        }

        writer = createWriter()
        writer.write('x_delim_data=True&x_relay_response=False&')
        writer.write('x_login=')
        writer.write(login)
        writer.write('&x_tran_key=')
        writer.write(transactionKey)
        writer.write('&x_delim_char=')
        writer.write(delimiter)
        writer.write('&x_encap_char=&x_encap_char=')
        writer.write('&x_currency_code=USD')
        switch (name) {
            case 'authorizeAndCapture':
                current = name
                writer.write('&x_method=CC&x_type=AUTH_CAPTURE')
                writer.write('&x_duplicate_window=')
                writer.write(duplicateWindow)
                writer.write('&x_test_request=')
                writer.write(testMode ? 'TRUE' : 'FALSE')
                break
            case 'authorizeOnly':
                writer.write('&x_method=CC&x_type=AUTH_ONLY')
                writer.write('&x_duplicate_window=')
                writer.write(duplicateWindow)
                writer.write('&x_test_request=')
                writer.write(testMode ? 'TRUE' : 'FALSE')
                break
            case 'capturePriorAuthorization':
                writer.write('&x_method=CC&x_type=PRIOR_AUTH_CAPTURE')
                writer.write('&x_duplicate_window=')
                writer.write(duplicateWindow)
                writer.write('&x_test_request=')
                writer.write(testMode ? 'TRUE' : 'FALSE')
                break
            case 'void':
                writer.write('&x_method=CC&x_type=VOID')
                break
            case 'echeckVoid':
                writer.write('&x_method=ECHECK&x_type=VOID')
                break
            case 'credit':
                writer.write('&x_method=CC&x_type=CREDIT')
                break
            case 'echeckCredit':
                writer.write('&x_method=ECHECK&x_type=CREDIT')
                break
            case 'echeck':
                writer.write('&x_method=ECHECK&x_type=AUTH_CAPTURE')
                break
            default:
                'unrecognized'
        }
        nodeCount++
    }

    def createNode(name, value) {
        String lowerCaseName = name.toLowerCase()
        boolean valueWritten = false
        switch (lowerCaseName) {
            case 'transactionid':
            case 'txid':
            case 'transid':
                writer.write('&x_trans_id=')
                break
            case 'amount':
                writer.write('&x_amount=')
                break
            case 'name':
            case 'fullname':
                String[] names = value.split(' ')
                writer.write('&x_first_name=')
                writer.write(names[0])
                writer.write('&x_last_name=')
                writer.write(names[1])
                valueWritten = true
                break
            case 'firstname':
                writer.write('&x_first_name=')
                break
            case 'lastname':
                writer.write('&x_last_name=')
                break
            case 'company':
                writer.write('&x_company=')
                break
            case 'city':
                writer.write('&x_city=')
                break
            case 'state':
                writer.write('&x_state=')
                break
            case 'zip':
            case 'zipcode':
                writer.write('&x_zip=')
                break
            case 'phone':
                writer.write('&x_phone=')
                break
            case 'customerId':
                writer.write('&x_cust_id=')
                break
            case 'email':
                writer.write('&x_email=')
                break
            case 'country':
                writer.write('&x_country=')
                break
            case 'address':
                writer.write('&x_address=')
                break
            case 'customerid':
                writer.write('&x_cust_id')
                break
            case 'invoice':
            case 'invoicenumber':
            case 'orderid':
            case 'ordernumber':
            case 'invoiceid':
                writer.write('&x_invoice_num=')
                break

            case 'cc':
            case 'creditcard':
            case 'creditcardnumber':
            case 'ccnumber':
            case 'cardnum':
                writer.write('&x_card_num=')
                break
            case 'cccode':
            case 'cvv':
            case 'cardcode':
                writer.write('&x_card_code=')
                break
            case 'expdate':
            case 'ccexpdate':
            case 'cardexpdate':
            case 'cardexpirationdate':
            case 'ccexperationdate':
                writer.write('&x_exp_date=')
                break
            default:
                if (lowerCaseName.startsWith('x_')) {
                    writer.write('&')
                    writer.write(lowerCaseName)
                } else {
                    writer.write('&x_')
                    if (lowerCaseName.indexOf('_') >= 0) {
                        writer.write(lowerCaseName)
                    } else {
                        writer.write("${name.replaceAll('([A-Z])', '_$1').toLowerCase()}=")
                    }

                }
        }
        if (!valueWritten) writer.write(value)
        nodeCount++
    }

    def createNode(name, Map attributes) {
        if (name.toLowerCase() == 'item') {
            writer.write('&x_line_item=')
            writer.write(attributes.get('id'))
            writer.write('<|>')
            writer.write(attributes.get('name'))
            writer.write('<|>')
            writer.write(attributes.get('description'))
            writer.write('<|>')
            writer.write(attributes.get('quantity'))
            writer.write('<|>')
            writer.write(attributes.get('price'))
            writer.write('<|>')
            writer.write(attributes.get('taxable'))
        }
    }

    def createNode(name, Map attributes, value) {
    }

    void setParent(parent, child) {
    }

    void nodeCompleted(parent, child) {
        nodeCount--
    }


}


