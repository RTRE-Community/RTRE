using Autodesk.Revit.Attributes;
using Autodesk.Revit.DB;
using Autodesk.Revit.UI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RevitPlugin
{
    [Transaction(TransactionMode.Manual)]
    public class GetListOfIfc : IExternalCommand
    {
        public Result Execute(ExternalCommandData commandData, ref string message, ElementSet elements)
        {
            RecieveListOfProjects();
            return Result.Succeeded;
        }

        public async void RecieveListOfProjects()
        {
            try
            {
                HttpClient client = new HttpClient();
                var url = "http://localhost:8080/api/GetProjectList";
                var response = await client.GetStringAsync(url);
                String content = response;
                TaskDialog.Show("List of Projects", content);
            }
            catch (Exception ex)
            {
                TaskDialog.Show("Failed", ex.Message);
            }
        }
    }
}
