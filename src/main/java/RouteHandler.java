import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteHandler {
    public static List<RouteParameter> toList(Map<Integer, List<String>> parametersData){
        List<RouteParameter> parameters = new ArrayList<>();

        int rowsToRemove = 4;
        for (int i = 0; i <= rowsToRemove; i++) {
            parametersData.remove(i);
        }

        for (Map.Entry row:
                parametersData.entrySet()) {

            String route = ((List<String>) row.getValue()).get(1);
            int from = (int) Float.parseFloat(((List<String>) row.getValue()).get(3));
            int to = (int) Float.parseFloat(((List<String>) row.getValue()).get(4));
            int technological = (int) Float.parseFloat(((List<String>) row.getValue()).get(6));
            int equalizing = (int) Float.parseFloat(((List<String>) row.getValue()).get(7));

            RouteParameter parameter = new RouteParameter(
                    Stations.FindPair(route),
                    from,
                    to,
                    technological,
                    equalizing
            );

            parameters.add(parameter);
        }

        return parameters;
    }

    public static RouteParameter findCompatible(List<RouteParameter> paramList, StationPair stationPair, int arriveTime){
        RouteParameter routeParameter = null;

        for (RouteParameter parameter : paramList) {
            if(stationPair.equals(parameter.getStationPair()) && arriveTime < parameter.getTimeTo()){
                routeParameter = parameter;
                break;
            }
        }

        return routeParameter;
    }
}
