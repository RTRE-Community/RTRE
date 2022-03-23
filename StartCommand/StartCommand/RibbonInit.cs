using Autodesk.Revit.UI;
using System.Reflection;

namespace StartCommand
{
    internal class RibbonInit
    {
        internal void AddRibbonPanel(UIControlledApplication application)
        {
            const string RIBBON_TAB = "RTRE";
            const string RIBBON_PANEL = "BimServer Tasks";

            TextBoxData itemData1 = new TextBoxData("itemName1");
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
                "StartCommand.PostIfc");

            PushButtonData b2Data = new PushButtonData(
                "Get", "Get" + System.Environment.NewLine + "Ifc File", thisAssemblyPath, "StartCommand.GetIfc");

            PushButton pb1 = (PushButton)ribbonPanel.AddItem(b1Data);
            TextBox item1 = (TextBox)ribbonPanel.AddItem(itemData1);
            PushButton pb2 = (PushButton)ribbonPanel.AddItem(b2Data);
            item1.ToolTip = itemData1.Name; // Can be changed to a more descriptive text.
            item1.ShowImageAsButton = true;
            item1.PromptText = "Input File Name here...";
            item1.EnterPressed += CallbackOfTextBox;
            pb1.ToolTip = "Post current project to IFC";
            pb2.ToolTip = " Recieve IFC file from bimServer";

        }
        public void CallbackOfTextBox(object sender, Autodesk.Revit.UI.Events.TextBoxEnterPressedEventArgs args)
        {
            Autodesk.Revit.UI.TextBox textBox = sender as Autodesk.Revit.UI.TextBox;

            TaskDialog.Show(textBox.Value.ToString(), textBox.Value.ToString());
            MyGlobals.userFileName = textBox.Value.ToString();
        }

        public static class MyGlobals
        {
            public static string userFileName= "default";
        }
    }
}