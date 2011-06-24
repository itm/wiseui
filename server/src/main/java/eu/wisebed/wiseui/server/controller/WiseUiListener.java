/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM), 
 *              Research Academic Computer Technology Institute (RACTI) 
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0 
 * (the "License"); you may not use this file except in compliance with the 
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.controller;

import com.google.inject.Inject;
import de.uniluebeck.itm.tr.util.StringUtils;
import de.uniluebeck.itm.wisebed.cmdlineclient.protobuf.ProtobufControllerClientListener;
import eu.wisebed.api.common.Message;
import eu.wisebed.api.controller.RequestStatus;
import eu.wisebed.wiseml.controller.WiseMLController;
import eu.wisebed.wiseml.model.WiseML;
import eu.wisebed.wiseml.model.setup.Node;
import eu.wisebed.wiseml.model.setup.Setup;
import eu.wisebed.wiseml.model.trace.Trace;
import eu.wisebed.wiseui.shared.dto.Status;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Soenke Nommensen
 */
public class WiseUiListener implements ProtobufControllerClientListener {

    private static final Logger LOG = LoggerFactory.getLogger(WiseUiListener.class);

    private Queue<eu.wisebed.wiseui.shared.dto.Message> messageQueue
            = new LinkedList<eu.wisebed.wiseui.shared.dto.Message>();

    private Trace wisemlTrace = new Trace();

    private Queue<eu.wisebed.wiseui.shared.dto.RequestStatus> requestStatusQueue
            = new LinkedList<eu.wisebed.wiseui.shared.dto.RequestStatus>();

    private Queue<String> notificationQueue = new LinkedList<String>();

    private List<String> nodeUrns;

    private WiseML wiseml;

    private Setup wisemlSetup;

    private Mapper mapper;

    @Inject
    public WiseUiListener(final Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onConnectionClosed() {
        LOG.debug("Connection closed");
    }

    @Override
    public void onConnectionEstablished() {
        LOG.debug("Connection established");
    }

    @Override
    public void experimentEnded() {
        LOG.debug("Experiment ended");
    }

    @Override
    public void receive(@WebParam(name = "msg", targetNamespace = "") List<Message> messages) {
        for (eu.wisebed.api.common.Message msg : messages) {

            // set experiment message
            final String source = msg.getSourceNodeId();
            final String timeStamp = msg.getTimestamp().toXMLFormat();
            final String data = StringUtils.toHexString(msg.getBinaryData());
            final String level = msg.getBinaryData()[1] == 0x00 ? "DEBUG" : "FATAL";
            LOG.info("Received Message :");
            LOG.info("[" + source + "][" + timeStamp + "][" + level + "][" + data + "]");


            // map message and put it in queue
            eu.wisebed.wiseui.shared.dto.Message message = mapper.map(msg, eu.wisebed.wiseui.shared.dto.Message.class);
            message.setTimestamp(
                    msg.getTimestamp().toGregorianCalendar().getTime());
            messageQueue.add(message);

            // set WiseML message
            eu.wisebed.wiseml.model.trace.Message traceMessage = new
                    eu.wisebed.wiseml.model.trace.Message();
            traceMessage.setTimestamp((long) msg.getTimestamp().getMillisecond());
            traceMessage.setData("[" + level + "][" + data + "]");
            traceMessage.setId(source);

            // add WiseML message to trace
            if (wisemlTrace.getChildren() == null) {
                wisemlTrace.setChildren(Trace.listFactory());
            }
            wisemlTrace.getChildren().add(traceMessage);

            LOG.info("Added to WiseML trace");
        }
    }

    @Override
    public void receiveNotification(@WebParam(name = "msg", targetNamespace = "") List<String> notifications) {
        for (String notification : notifications) {
            LOG.info("[Notification][" + notification + "]");
            notificationQueue.add(notification);
        }
    }

    @Override
    public void receiveStatus(@WebParam(name = "status", targetNamespace = "") List<RequestStatus> requestStatuses) {
        for (eu.wisebed.api.controller.RequestStatus requestStatus : requestStatuses) {

            eu.wisebed.wiseui.shared.dto.RequestStatus reqStatusDto
                    = mapper.map(requestStatus, eu.wisebed.wiseui.shared.dto.RequestStatus.class);

            for (int i = 0; i < requestStatus.getStatus().size(); i++) {
                eu.wisebed.api.controller.Status status =
                        requestStatus.getStatus().get(i);
                final String requestID = requestStatus.getRequestId();
                final String msg = status.getMsg();
                final String nodeID = status.getNodeId();
                final String value = status.getValue().toString();

                LOG.info("[RequestStatus][" + requestID + "]" +
                        "[status msg][" + msg + "]" +
                        "[node][" + nodeID + "]" +
                        "[value][" + value + "]");

                reqStatusDto.getStatus().add(mapper.map(status, Status.class));
            }
            requestStatusQueue.add(reqStatusDto);
        }
    }

    public String getSerializedWiseMl() {
        List<Node> nodeList = new ArrayList<Node>();
        for (String nodeUrn : nodeUrns) {
            Node node = new Node();
            node.setId(nodeUrn);
            nodeList.add(node);
        }
        wisemlSetup.setNodes(nodeList);
        wiseml.setSetup(wisemlSetup);
        LOG.info("WiseML Setup is now prepared");

        // prepare wiseml trace
        wiseml.setTrace(wisemlTrace);

        // return serialiased wiseml
        return new WiseMLController().writeWiseMLAsSTring(wiseml);
    }

    public Queue<eu.wisebed.wiseui.shared.dto.Message> getMessageQueue() {
        return messageQueue;
    }

    public Trace getWisemlTrace() {
        return wisemlTrace;
    }

    public Queue<eu.wisebed.wiseui.shared.dto.RequestStatus> getRequestStatusQueue() {
        return requestStatusQueue;
    }

    public Queue<String> getNotificationQueue() {
        return notificationQueue;
    }

    public List<String> getNodeUrns() {
        return nodeUrns;
    }

    public void setNodeUrns(List<String> nodeUrns) {
        this.nodeUrns = nodeUrns;
    }

    public WiseML getWiseml() {
        return wiseml;
    }

    public void setWiseml(WiseML wiseml) {
        this.wiseml = wiseml;
    }

    public Setup getWisemlSetup() {
        return wisemlSetup;
    }

    public void setWisemlSetup(Setup wisemlSetup) {
        this.wisemlSetup = wisemlSetup;
    }
}
