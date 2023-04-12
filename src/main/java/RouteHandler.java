import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteHandler {
    public static List<RouteParameter> getRouteParameters(Map<Integer, List<String>> parametersData){
        List<RouteParameter> parameters = new ArrayList<>();

        int rowsToRemove = 4;
        for (int i = 0; i <= rowsToRemove; i++) {
            parametersData.remove(i);
        }

        for (Map.Entry row:
                parametersData.entrySet()) {

            String lineID = ((List<String>) row.getValue()).get(0);
            int from = (int) Float.parseFloat(((List<String>) row.getValue()).get(3));
            int to = (int) Float.parseFloat(((List<String>) row.getValue()).get(4));
            int technological = (int) Float.parseFloat(((List<String>) row.getValue()).get(6));
            int equalizing = (int) Float.parseFloat(((List<String>) row.getValue()).get(7));

            RouteParameter parameter = new RouteParameter(
                    lineID,
                    from,
                    to,
                    technological,
                    equalizing
            );

            parameters.add(parameter);
        }

        return parameters;
    }

    public static RouteParameter findCompatible(List<RouteParameter> paramList, String tripType, int arriveTime){
        RouteParameter routeParameter = null;

        for (RouteParameter param : paramList) {
            if(tripType.equals(param.getCompatibleTripType()) && arriveTime < param.getTimeTo()){
                routeParameter = param;
                break;
            }
        }

        return routeParameter;
    }
}
