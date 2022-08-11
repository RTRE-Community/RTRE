package fore.rtre.server.Helper_Classes;

import fore.rtre.server.Object.Helper.sortByDate;
import fore.rtre.server.Object.SubProjectMeta;
import fore.rtre.server.config.BimserverConfig;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;

import java.util.ArrayList;
import java.util.List;

public class LatestProjectGetter {

    public static long getLatestProjectWithParentId(Long parentId) throws ServerException, UserException {

        List<SubProjectMeta> subProjectMetaList = new ArrayList<>();
        List<Long> subprojectList = BimserverConfig.client.getServiceInterface().getProjectByPoid(parentId).getSubProjects();
        for (Long id : subprojectList) {
            SProject temp = BimserverConfig.client.getServiceInterface().getProjectByPoid(id);
            subProjectMetaList.add(
                    new SubProjectMeta(temp.getOid(),
                            temp.getCreatedDate(),
                            temp.getName(),
                            temp.getDescription()
                    )
            );
        }

        subProjectMetaList.sort(new sortByDate());

        return subprojectList.get(subprojectList.size() - 1);
    }
}
