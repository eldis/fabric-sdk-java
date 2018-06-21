package org.hyperledger.fabric.sdk;

import java.io.InputStream;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

/**
 * InstallPackageProposalRequest.
 */
public class InstallPackageProposalRequest extends TransactionRequest {

    private InputStream chaincodeInputStream = null;

    InstallPackageProposalRequest(User userContext) {
        super(userContext);
    }

    public InputStream getChaincodeInputStream() {
        return chaincodeInputStream;
    }

    /**
     * Chaincode input stream containing the actual chaincode. Only format supported is a deployment spec.
     * The contents of the stream are not validated or inspected by the SDK.
     *
     * @param chaincodeInputStream
     * @throws InvalidArgumentException
     */

    public void setChaincodeInputStream(InputStream chaincodeInputStream) throws InvalidArgumentException {
        if (chaincodeInputStream == null) {
            throw new InvalidArgumentException("Chaincode input stream may not be null.");
        }
        this.chaincodeInputStream = chaincodeInputStream;
    }
}