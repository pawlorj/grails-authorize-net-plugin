package com.vinomis.authnet
/**
 *
 * @author Bob Pawlowski
 */
class AuthorizeNetResponse {

    static def paramNames = [
        'responseCode', 'responseSubCode','responseReasonCode','responseReasonText','authorizationCode',
        'avsResponse', 'transactionId', 'invoiceNumber', 'description', 'amount',
        'method', 'type', 'customerId', 'billingFirstName','billingLastName',
        'billingCompany', 'billingAddress','billingCity','billingState','billingZipCode',
        'billingCountry','billingPhone','billingFax','email','shippingFirstName',
        'shippingLastName','shippingCompany','shippingAddress','shippingCity', 'shippingState',
        'shippingZipCode', 'shippingCountry', 'tax', 'duty','freight',
        'taxExempt','purchaseOrderNumber','md5Hash','cardCodeResponse','cardholderAuthenticationVerificationResponse'
    ]

    static def responseCodes = ['1': 'Approved', '2': 'Declined', '3': 'Error', '4':'Held for Review']
    String delimiter = ';'


    static AuthorizeNetResponse fromHistory(String historyEntry) {
        def r = new AuthorizeNetResponse()
        if (!historyEntry) return r
        def index = 0
        historyEntry.split('\n').each() { param ->
            if (index < paramNames.size()) {
                def attrVal = param.split('=')
                r[paramNames[index]] = attrVal.size() == 2 ? attrVal[1] : ''
                index++
            }
        }

        r
    }

    AuthorizeNetResponse() {}

    AuthorizeNetResponse(String responseText, String newDelimiter = ';') {
        this.delimiter = newDelimiter
        def params = responseText.split(delimiter)
        def index = 0
        params.each() { param ->
            if (index < paramNames.size()) {
                this[paramNames[index]] = param
                index++
            }
        }
    }

    String dump() {
        String t = ""
        paramNames.each() { attr ->
            t+= "${attr}=${this[attr]}\n"
        }

        return t
    }

    String toString() {
        "${type},${responseCodeText},\$${amount},TxID: ${transactionId}"
    }

    String getResponseCodeText() {
        responseCodes[responseCode]
    }

    String responseCode //1
    String responseSubCode //2
    String responseReasonCode //3
    String responseReasonText //4
    String authorizationCode // 5
/*
AVS Response
The Address Verification Service (AVS) response code
A = Address (Street) matches, ZIP does not
B = Address information not provided for AVS check
E = AVS error
G = Non-U.S. Card Issuing Bank
N = No Match on Address (Street) or ZIP
P = AVS not applicable for this transaction
R = Retry — System unavailable or timed out
S = Service not supported by issuer
U = Address information is unavailable
W = Nine digit ZIP matches, Address (Street) does not
X = Address (Street) and nine digit ZIP match
Y = Address (Street) and five digit ZIP match
Z = Five digit ZIP matches, Address (Street) does not
Indicates the result of the AVS filter.
For more information about AVS, see the Merchant Integration Guide.
*/
    String avsResponse //6
    String transactionId //7
    String invoiceNumber //8
    String description //9
    String amount //10

    String method //11, CC or ECHECK
    String type //12, AUTH_CAPTURE, AUTH_ONLY, CAPTURE_ONLY, CREDIT, PRIOR_AUTH_CAPTURE, VOID
    String customerId  //13
    String billingFirstName  //14
    String billingLastName //15

    String billingCompany //16
    String billingAddress //17
    String billingCity //18
    String billingState //19
    String billingZipCode  //20
    String billingCountry //21
    String billingPhone //22
    String billingFax //23
    String email //24

    String shippingFirstName  //25
    String shippingLastName //26
    String shippingCompany //27
    String shippingAddress //28
    String shippingCity //29
    String shippingState //30
    String shippingZipCode  //31
    String shippingCountry //32

    String tax //33
    String duty  //34
    String freight //35

    /*
    TRUE, FALSE,
    T, F,
    YES, NO,
    Y, N,
    1, 0
    */
    String taxExempt  //36
    String purchaseOrderNumber //37
    String md5Hash  //38

    /*
    Card Code Response
The card code verification (CCV) response code
M = Match
N = No Match
P = Not Processed
S = Should have been present
U = Issuer unable to process request
Indicates the result of the CCV filter.
For more information about CCV, see the Merchant Integration Guide.
*/
    String cardCodeResponse //39

    /*
Cardholder Authentication Verification Response
The cardholder authentication verification response code
Blank or not present = CAVV not validated
0 = CAVV not validated because erroneous data was submitted
1 = CAVV failed validation
2 = CAVV passed validation
3 = CAVV validation could not be performed; issuer attempt incomplete
4 = CAVV validation could not be performed; issuer system error
5 = Reserved for future use
6 = Reserved for future use
7 = CAVV attempt — failed validation — issuer available (U.S.-issued card/non-U.S acquirer)
8 = CAVV attempt — passed validation — issuer available (U.S.-issued card/non-U.S. acquirer)
9 = CAVV attempt — failed validation — issuer unavailable (U.S.-issued card/non-U.S. acquirer)
A = CAVV attempt — passed validation — issuer unavailable (U.S.-issued card/non-U.S. acquirer)
B = CAVV passed validation, information only, no liability shift
    */
    String cardholderAuthenticationVerificationResponse //40
}







