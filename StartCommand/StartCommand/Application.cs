using Autodesk.Revit.UI;
using System.Drawing;
using System.Drawing.Imaging;
using System.Reflection;
using System.Windows.Media.Imaging;

namespace StartCommand
{
    public class Application : IExternalApplication
    {
        public Result OnShutdown(UIControlledApplication application)
        {
            throw new NotImplementedException();
        }

        public Result OnStartup(UIControlledApplication application)
        {
            RibbonInit creator = new RibbonInit();
            creator.AddRibbonPanel(application);

            return Result.Succeeded;
        }

    }



}
