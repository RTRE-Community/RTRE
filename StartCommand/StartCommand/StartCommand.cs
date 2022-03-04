﻿using Autodesk.Revit.Attributes;
using Autodesk.Revit.DB;
using Autodesk.Revit.UI;
using System.Text;

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
                doc.Export("C:/Users/Levan/Documents/RTRECOM", "IFCfilerevite", exportOptions);
                tr.Commit();
              

                HttpClient client = new HttpClient();

                var data = new StringContent("Hello from Revit", Encoding.UTF8, "application/json");
                // change url
                var url = "https://ptsv2.com/t/a8t2d-1645446841/post";
                var response = await client.PostAsync(url, data);

                string result = response.Content.ReadAsStringAsync().Result;

                TaskDialog.Show("Success", result);
            }
            catch (Exception e)
            {
                TaskDialog.Show("Failed", e.Message);
            }
        }
    }
}
