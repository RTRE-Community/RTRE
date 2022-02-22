using Autodesk.Revit.UI;
using System.Reflection;

namespace StartCommand
{
    internal class RibbonInit
    {
        internal void AddRibbonPanel(UIControlledApplication application)
        {
            const string RIBBON_TAB = "RTRE";
            const string RIBBON_PANEL = "Sever Tasks";

            application.CreateRibbonTab(RIBBON_TAB);

            // Add a new ribbon panel
            RibbonPanel ribbonPanel = application.CreateRibbonPanel(RIBBON_TAB, RIBBON_PANEL);

            // Get dll assembly path
            string thisAssemblyPath = Assembly.GetExecutingAssembly().Location;

            // create push button for CurveTotalLength
            PushButtonData b1Data = new PushButtonData(
                "POST",
                "POST" + System.Environment.NewLine + "  Request  ",
                thisAssemblyPath,
                "StartCommand.StartCommand");

            PushButton pb1 = (PushButton)ribbonPanel.AddItem(b1Data);
            pb1.ToolTip = "Communicate with the server!";

        }

 
    }
}