using Autodesk.Revit.Attributes;
using Autodesk.Revit.DB;
using Autodesk.Revit.UI;
using System.Text;
using static StartCommand.RibbonInit;

namespace StartCommand
{
    [Transaction(TransactionMode.Manual)]
    public class PostIfc: IExternalCommand

    {
        public Result Execute(ExternalCommandData commandData, ref string message, ElementSet elements)
        {
            pingServer(commandData);

            return Result.Succeeded;
        }

        private async void pingServer(ExternalCommandData commandData)
        {
            String directory = "bimserverdir";

            UIApplication uiapp = commandData.Application;
            UIDocument uidoc = uiapp.ActiveUIDocument;
            Document doc = uidoc.Document;
            View view = uidoc.ActiveView;

            try
            {

                Transaction tr = new Transaction(commandData.Application.ActiveUIDocument.Document); tr.Start("Command name here");

                IFCExportOptions exportOptions = new IFCExportOptions();
                doc.Export(directory , MyGlobals.userFileName, exportOptions);
                tr.Commit();
              

                HttpClient client = new HttpClient();

                var data = new StringContent("Hello from Revit", Encoding.UTF8, "application/json");
                // change url
                var url = "http://localhost:8080/api/postIfc?fileName="+ MyGlobals.userFileName;
                var response = await client.GetAsync(url);

                TaskDialog.Show("Success", "Current file has been posted to Bim Server");
            }
            catch (Exception e)
            {
                TaskDialog.Show("Failed", e.Message);
            }
        }
    }
}
