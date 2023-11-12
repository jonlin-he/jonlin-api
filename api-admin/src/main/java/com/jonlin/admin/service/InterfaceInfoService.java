package com.jonlin.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jonlin.apicommon.model.entity.InterfaceInfo;

/**
* @author 28091
* @description 针对表【interface_info(`interface_info`)】的数据库操作Service
* @createDate 2023-11-03 08:28:46
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 参数校验
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 更新总调用数
     * @param interfaceId 接口id
     * @return boolean
     */
    boolean updateTotalInvokes(long interfaceId);

}
