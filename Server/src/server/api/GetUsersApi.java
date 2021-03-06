/*
 * 
 */
package server.api;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.core.ApiMethod;
import server.core.HttpCode;
import server.entity.User;
import server.logic.UserDAO;

/**
 *
 * @author oglandx
 * 
 * Enables to receive http-requests with many IDs or social tokens,
 * and sends all requested information of users.
 */
public class GetUsersApi implements ApiMethod{

    @Override
    public ApiAnswer execute(Map<String, String> params) {
        boolean isParamIDs = params.get("ids") != null;
        final String param = isParamIDs ? 
                params.get("ids") : params.get("SocialTokens");
        final String [] splitted = param.split("\\$");
        StringBuilder answer = new StringBuilder("[");
        for (String element: splitted){
            try{
                User user = isParamIDs ?
                        UserDAO.getById(Long.parseLong(element)):
                        UserDAO.getByToken(element);
                answer.append(user.asJSON().toJSONString());
                answer.append(",");
            } catch(Exception e){
                Logger.getLogger(VKApi.class.getName())
                                .log(Level.SEVERE, null, e);
            }
        }
        if(answer.length() != 1){
            answer.replace(answer.length() - 1, answer.length(), "]");
        }
        else{
            answer.append("]");
        }
        return new ApiAnswer(HttpCode.OK, answer.toString());
    }
}
