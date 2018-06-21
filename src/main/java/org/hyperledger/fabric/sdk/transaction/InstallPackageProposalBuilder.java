package org.hyperledger.fabric.sdk.transaction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.ByteString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.protos.peer.FabricProposal;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.helper.Config;
import org.hyperledger.fabric.sdk.helper.DiagnosticFileDumper;

import static java.lang.String.format;

public class InstallPackageProposalBuilder extends LSCCProposalBuilder {

    private static final Log logger = LogFactory.getLog(InstallPackageProposalBuilder.class);
    private static final boolean IS_TRACE_LEVEL = logger.isTraceEnabled();

    private static final Config config = Config.getConfig();
    private static final DiagnosticFileDumper diagnosticFileDumper = IS_TRACE_LEVEL
            ? config.getDiagnosticFileDumper() : null;

    protected String action = "install";
    private InputStream chaincodeInputStream;

    protected InstallPackageProposalBuilder() {
        super();
    }

    public static InstallPackageProposalBuilder newBuilder() {
        return new InstallPackageProposalBuilder();
    }

    @Override
    public FabricProposal.Proposal build() throws ProposalException, InvalidArgumentException {
        constructInstallPackageProposal();
        return super.build();
    }

    private void constructInstallPackageProposal() throws ProposalException {
        try {
            createNetModeTransaction();
        } catch (IOException exp) {
            logger.error(exp);
            throw new ProposalException("IO Error while creating install proposal", exp);
        }
    }

    private void createNetModeTransaction() throws IOException {
        logger.debug("createNetModeTransaction");

        if (chaincodeInputStream == null) {
            throw new IllegalArgumentException("Missing chaincodeInputStream in InstallPackageRequest");
        }

        ByteString data = ByteString.readFrom(chaincodeInputStream);

        if (null != diagnosticFileDumper) {
            logger.trace(format("Installing chaincode from input stream tar file dump %s",
                diagnosticFileDumper.createDiagnosticTarFile(data.toByteArray())));
        }

        // set args
        final List<ByteString> argList = new ArrayList<>();
        argList.add(ByteString.copyFrom(action, StandardCharsets.UTF_8));
        argList.add(data);
        args(argList);
    }

    public InstallPackageProposalBuilder setChaincodeInputStream(InputStream chaincodeInputStream) {
        this.chaincodeInputStream = chaincodeInputStream;

        return this;
    }
}