using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Autodesk.Revit.Attributes;
using Autodesk.Revit.DB;
using Autodesk.Revit.UI;
using static StartCommand.RibbonInit;

namespace StartCommand
{
    [Transaction(TransactionMode.Manual)]
    public class GetIfc : IExternalCommand
    {
        public Result Execute(ExternalCommandData commandData, ref string message, ElementSet elements)
        {
            recieveIfcFile(commandData);
            return Result.Succeeded;
        }

        private async void recieveIfcFile(ExternalCommandData commandData)
        {

            String directory = "";
            try
            {
                HttpClient client = new HttpClient();
                var url = "http://localhost:8080/api/getIfc?fileName="+ MyGlobals.userFileName;
                var response = await client.GetAsync(url);
                TaskDialog.Show("Success", "File has been saved as imHere.ifc");
            }
            catch (Exception ex)
            {
                TaskDialog.Show("Failed", ex.Message);
            }

        }
    }
}
