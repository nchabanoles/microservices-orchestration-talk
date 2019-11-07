package bonita.kafka.watcher;

import com.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.engine.util.APITypeManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BPMMessagePublisher {

    static {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("server.url", "http://localhost:8080");
        parameters.put("application.name", "bonita");
        parameters.put("connections.max", "5");
        APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, parameters);
    }

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BPMMessagePublisher.class);

    public boolean sendBPMMessageFromPayload(String payload) {
        APIClient apiClient = new APIClient();
        try {
            apiClient.login(1, "walter.bates", "bpm");

            String messageName = buildMessageNameFromPayload(payload);
            Expression targetProcess = new ExpressionBuilder().createConstantStringExpression("New Order");
            Expression targetFlowNode = buildFlowNodeNameExpressionFrom(messageName);
            Expression idKey = new ExpressionBuilder().createConstantStringExpression("id");
            Expression timestampKey = new ExpressionBuilder().createConstantStringExpression("timestamp");
            Map<Expression, Expression> messageContent = new HashMap<>();
            Expression orderId = buildIDValueFromPayload(payload);
            messageContent.put(idKey, orderId);
            messageContent.put(timestampKey,buildNowExpression());
            Map<Expression, Expression> correlations = buildCorrelationsFrom(messageName,orderId);
            if(correlations!=null) {
                apiClient.getProcessAPI().sendMessage(messageName, targetProcess, targetFlowNode, messageContent, correlations);
            } else {
                apiClient.getProcessAPI().sendMessage(messageName, targetProcess, targetFlowNode, messageContent);
            }
            LOGGER.info(MessageFormat.format("Message \"{0}\" sent to process \"{1}\" (flownode: \"{2}\") (restricted to \"{4}\") with content: \"{3}\"",messageName, targetProcess.getContent(), targetFlowNode.getContent(),messageContent,correlations));

            apiClient.logout();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Map<Expression, Expression> buildCorrelationsFrom(String messageName, Expression orderId) {
        if("New Order Available".equals(messageName)) {
            return null;
        }
        HashMap<Expression, Expression> result = new HashMap<>();
        try {
            result.put(new ExpressionBuilder().createConstantStringExpression("orderId"),orderId);
        } catch (InvalidExpressionException e) {
            LOGGER.error("Unable to build correlation keys, ignoring error (may result in messages not consumed in Bonita !!!)");
            return null;
        }
        return result;
    }

    private String buildMessageNameFromPayload(String payload) {
        return new JSONObject(payload).getString("name");
    }

    private Expression buildFlowNodeNameExpressionFrom(String s) throws InvalidExpressionException {
        return new ExpressionBuilder().createConstantStringExpression(s);
    }

    private Expression buildIDValueFromPayload(String payload) {
        String idValue = new JSONObject(payload).getString("id");
        try {
            return new ExpressionBuilder().createConstantStringExpression(idValue);
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Expression buildNowExpression() {
        try {
            return new ExpressionBuilder().createConstantStringExpression(new Date().toString());
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
