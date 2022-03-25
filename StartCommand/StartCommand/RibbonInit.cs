using Autodesk.Revit.UI;
using System.Reflection;

namespace RevitPlugin
{
    internal class RibbonInit
    {
        internal void AddRibbonPanel(UIControlledApplication application)
        {
            const string RIBBON_TAB = "RTRE";
            const string RIBBON_PANEL = "BimServer Tasks";

            //Create ribbon Tab
            application.CreateRibbonTab(RIBBON_TAB);

            // Add a new ribbon panel
            RibbonPanel ribbonPanel = application.CreateRibbonPanel(RIBBON_TAB, RIBBON_PANEL);

            // Get dll assembly path
            string thisAssemblyPath = Assembly.GetExecutingAssembly().Location;

            // Creating data for inputs
            TextBoxData itemData1 = new TextBoxData("itemName1");
            PushButtonData b1Data = new PushButtonData(
                "POST", "POST" + System.Environment.NewLine + "  Request  ", thisAssemblyPath, "RevitPlugin.PostIfc");
            PushButtonData b2Data = new PushButtonData(
                "Get", "Get" + System.Environment.NewLine + "Ifc File", thisAssemblyPath, "RevitPlugin.GetIfc");
            PushButtonData b3Data = new PushButtonData(
                "GetList", "GetList" + System.Environment.NewLine + "Ifc File", thisAssemblyPath, "RevitPlugin.GetListOfIfc");

            // Add data to ribbon
            PushButton pb1 = (PushButton)ribbonPanel.AddItem(b1Data);
            TextBox item1 = (TextBox)ribbonPanel.AddItem(itemData1);
            PushButton pb2 = (PushButton)ribbonPanel.AddItem(b2Data);
            PushButton pb3 = (PushButton) ribbonPanel.AddItem(b3Data);

            //Additional information for inputField/buttons
            item1.ToolTip = itemData1.Name; // Can be changed to a more descriptive text.
            item1.ShowImageAsButton = true;
            item1.PromptText = "Input File Name here...";
            item1.EnterPressed += CallbackOfTextBox;
            pb1.ToolTip = "Post current project to IFC";
            pb2.ToolTip = " Recieve IFC file from bimServer";
            pb3.ToolTip = "Get list of all active projects";

        }
        public void CallbackOfTextBox(object sender, Autodesk.Revit.UI.Events.TextBoxEnterPressedEventArgs args)
        {

            // Method saves user input as a global variable
            TextBox textBox = (TextBox)sender;
            TaskDialog.Show(textBox.Value.ToString(), textBox.Value.ToString());
            MyGlobals.userFileName = textBox.Value.ToString();
        }

        public static class MyGlobals
        {
            public static string userFileName = "default";
        }
    }
}