package com.lms.sqlfather.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.userInterfaceInfo.PageUserInterfaceInfoRequest;
import com.lms.lmscommon.model.entity.UserInterfaceInfo;
import com.lms.lmscommon.model.vo.userInterfaceInfo.UserInterfaceInfoVO;

/**
 *
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
//    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);
//
//    /**
//     * 调用接口统计
//     * @param interfaceInfoId
//     * @param userId
//     * @return
//     */
//    boolean invokeCount(long interfaceInfoId, long userId);


    Boolean saveUserInterface(Long uid,Long interfaceId);

    Page<UserInterfaceInfoVO> pageUseInterfaceInfo(PageUserInterfaceInfoRequest pageInterfaceInfoDto);

    Page<UserInterfaceInfoVO> pageCurrentInterfaceInfo(Long uid, PageUserInterfaceInfoRequest pageInterfaceInfoDto);
}
