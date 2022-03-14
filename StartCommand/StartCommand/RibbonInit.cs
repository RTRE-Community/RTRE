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
                "StartCommand.StartCommand");

            PushButton pb1 = (PushButton)ribbonPanel.AddItem(b1Data);
            Autodesk.Revit.UI.TextBox item1 = ribbonPanel.AddItem(itemData1) as Autodesk.Revit.UI.TextBox;
            item1.Value = "Input something here...";
            item1.ToolTip = itemData1.Name; // Can be changed to a more descriptive text.
            item1.ShowImageAsButton = true;
            item1.EnterPressed += CallbackOfTextBox;
            pb1.ToolTip = "Communicate with the server!";

        }
        public void CallbackOfTextBox(object sender, Autodesk.Revit.UI.Events.TextBoxEnterPressedEventArgs args)
        {
            Autodesk.Revit.UI.TextBox textBox = sender as Autodesk.Revit.UI.TextBox;

            TaskDialog.Show(textBox.Value.ToString(), textBox.Value.ToString());
        }

    }
}