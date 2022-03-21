using Autodesk.Revit.Attributes;
using Autodesk.Revit.DB;
using Autodesk.Revit.UI;
using System.Text;
using static StartCommand.RibbonInit;

namespace StartCommand
{
    [Transaction(TransactionMode.Manual)]
    public class StartCommand: IExternalCommand

    {
        public Result Execute(ExternalCommandData commandData, ref string message, ElementSet elements)
        {
            pingServer(commandData);

            return Result.Succeeded;
        }

        private async void pingServer(ExternalCommandData commandData)
        {
            UIApplication uiapp = commandData.Application;

            UIDocument uidoc = uiapp.ActiveUIDocument;


            Document doc = uidoc.Document;

            View view = uidoc.ActiveView;

            try
            {

                Transaction tr = new Transaction(commandData.Application.ActiveUIDocument.Document); tr.Start("Command name here");

                IFCExportOptions exportOptions = new IFCExportOptions();
                doc.Export("BIMSERVER DIRECTORY", MyGlobals.userFileName, exportOptions);
                tr.Commit();
              

                HttpClient client = new HttpClient();

                var data = new StringContent("Hello from Revit", Encoding.UTF8, "application/json");
                // change url
                var url = "http://localhost:8080/api/postIfc?fileName="+ MyGlobals.userFileName;
                var response = await client.GetAsync(url);

            }
            catch (Exception e)
            {
                TaskDialog.Show("Failed", e.Message);
            }
        }
    }
}
