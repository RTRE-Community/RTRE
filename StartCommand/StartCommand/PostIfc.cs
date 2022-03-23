using Autodesk.Revit.Attributes;
using Autodesk.Revit.DB;
using Autodesk.Revit.UI;
using System.Text;
using static RevitPlugin.RibbonInit;

namespace RevitPlugin
{
    [Transaction(TransactionMode.Manual)]
    public class PostIfc : IExternalCommand

    {
        public Result Execute(ExternalCommandData commandData, ref string message, ElementSet elements)
        {
            pingServer(commandData);

            return Result.Succeeded;
        }

        private async void pingServer(ExternalCommandData commandData)
        {
            String directory = "bimserverdir";

            //Open for Transactions on current file
            UIApplication uiapp = commandData.Application;
            UIDocument uidoc = uiapp.ActiveUIDocument;
            Document doc = uidoc.Document;
            View view = uidoc.ActiveView;

            try
            {
                //Start Transaction
                Transaction tr = new Transaction(commandData.Application.ActiveUIDocument.Document); tr.Start("Command name here");

                //Export file as IFC file
                IFCExportOptions exportOptions = new IFCExportOptions();
                doc.Export(directory, MyGlobals.userFileName, exportOptions);
                tr.Commit();

                // Create web client connection
                HttpClient client = new HttpClient();
                var url = "http://localhost:8080/api/postIfc?fileName=" + MyGlobals.userFileName;
                // Send and recieve from API
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
