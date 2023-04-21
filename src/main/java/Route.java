import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Route {
    public static List<RouteParameter> getListFromExcel(){
        Excel excel = Excel.getInstance();
        Map<Integer, List<String>> parametersData = excel.getDataFrom(excel.defaultWorkbook(), 0);

        //Remove unnecessary header lines
        int rowsToRemove = 4;
        for (int i = 0; i <= rowsToRemove; i++) {
            parametersData.remove(i);
        }

        //Build route parameter list
        List<RouteParameter> parameters = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> row:
                parametersData.entrySet()) {

            String route = row.getValue().get(1);
            int from = (int) Float.parseFloat(row.getValue().get(3));
            int to = (int) Float.parseFloat(row.getValue().get(4));
            int technological = (int) Float.parseFloat(row.getValue().get(6));
            int equalizing = (int) Float.parseFloat(row.getValue().get(7));

            RouteParameter parameter = new RouteParameter(
                    Terminals.FindPair(route),
                    from,
                    to,
                    technological,
                    equalizing
            );

            parameters.add(parameter);
        }

        return parameters;
    }

    public static RouteParameter findCompatible(List<RouteParameter> paramList, Terminals terminals, int arriveTime){
        RouteParameter routeParameter = null;

        for (RouteParameter parameter : paramList) {
            if(terminals.equals(parameter.getTerminals()) && arriveTime < parameter.getTimeTo()){
                routeParameter = parameter;
                break;
            }
        }

        return routeParameter;
    }
}
