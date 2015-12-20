package core_lib.engine_helper.project;

import core_lib.domainbean_model.UrlConstantForThisProject;
import core_lib.simple_network_engine.engine_helper.interfaces.ISpliceFullUrlByDomainBeanSpecialPath;

public class SpliceFullUrlByDomainBeanSpecialPath implements ISpliceFullUrlByDomainBeanSpecialPath {

    public SpliceFullUrlByDomainBeanSpecialPath() {
    }

    @Override
    public String fullUrlByDomainBeanSpecialPath(final String specialPath) {
        return UrlConstantForThisProject.MainUrl;
    }
}
