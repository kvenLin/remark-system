package com.uchain.remarksystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
public enum CodeMsg {
    AUTHENTICATION_ERROR(401,"用户认证失败,请重新登录" ),
    PASSWORD_ERROR(402, "密码错误"),
    PERMISSION_DENNY(403,"权限不足" ),
    NOT_FOUND(404,"url错误,请求路径未找到" ),
    REQUEST_METHOD_ERROR(550,"不支持%s的请求方式" ),
    SERVER_ERROR(500,"服务器未知错误:%s" ),
    USER_ALREADY_EXIST(501, "该员工号已存在"),
    ADD_ERROR(502, "添加异常"),
    USER_NOT_EXIST(503,"用户不存在" ),
    UPDATE_ERROR(504,"更新异常" ),
    PAGE_NUM_ERROR(505,"分页错误,页数必须大于等于1" ),
    ROLE_NOT_EXIST(506, "角色不存在"),
    PARAM_IS_NULL(507,"参数不能为空" ),
    SEND_CODE_ERROR(508,"生成验证码失败" ),
    CHECK_CODE_ERROR(509,"验证码校验错误" ),
    BIND_ERROR(511,"参数校验错误:%s"),
    VERIFY_CODE_ERROR(512,"验证码错误" ),
    HEADER_COLUMN_EXIST(513, "当前表列已存在表头信息"),
    CHOICE_NUM_EXIST(514,"该选项已存在" ),
    USER_CHECKING(515,"用户正在审核中" ),
    CHOICE_NOT_EXIST(516,"该选项不存在" ),
    HEADER_NOT_EXIST(517,"表头不存在" ),
    PROJECT_NOT_EXIST(518,"项目不存在" ),
    PROJECT_PROCESSING(519,"项目已经启动无法进行操作" ),
    USER_PROJECT_EXIST(520,"用户已经参与该项目,无法再次添加相同项目"),
    CHECK_PASSWORD_ERROR(521,"两次密码不一致" ),
    NEW_PASSWORD_SAME(522,"新密码与原密码一致" ),
    PROJECT_DATA_EXIST(523,"该项目数据已经存在,不能重复导入" ),
    FILE_FORMAT_ERROR(524,"文件格式错误,只能上传.xls和.xlsx格式" ),
    HEADER_NOT_MATCH(525,"表头与当前文件数据格式不匹配" ),
    PACKAGE_NOT_EXIST(526,"数据包不存在" ),
    DATA_GRAB_ERROR(527, "数据抓取异常"),
    DATA_CLEAN(528,"数据已分配完毕"),
    PACKAGE_PROCESSING(529,"当前用户有数据包未完成不能进行删除" ),
    ONE_PACKAGE_UN_FINISH(530,"需要完成当前项目已经抓取的数据后才能进行再次的抓取" ),
    GRAB_PERMISSION_DENY(531,"非指定项目参与的用户无法进行操作" ),
    PROJECT_FINISHED(532,"项目已经完成,无法进行操作项目数据" ),
    DATA_ANSWER_NOT_FINISH(533,"当前数据标注还未完成" ),
    PACKAGE_STATUS_ERROR(534,"数据包状态错误,无法操作" ),
    DATA_NOT_EXIST(535,"数据不存在" ),
    CHOICE_NOT_IN_PROJECT(536,"该选项不存在于该项目" ),
    NO_PACKAGE_TO_CREATE_EXCEL(537,"不存在已完成的数据包,无法导出项目数据表" ),
    PROJECT_NOT_START(538,"项目未启动,无法执行导出操作" ),
    ANSWER_NO_EXIST(539,"回答不存在,不能进行更新操作"),
    ANSWER_ALREADY_EXIST(540,"答案已经提交过,请修改内容后再次提交"),
    PACKAGE_COMMIT_TO_CHECK(541,"非标注状态,无法进行编辑"),
    STATUS_OVER_ROLE(542,"无法查看当前状态的数据包"),
    NOT_BID_USER(543,"不是标注员身份无法操作" ),
    DOWNLOAD_FILE_ERROR(544, "文件下载异常"),
    PAGE_NO_DATA(545,"超出分页数，没有数据" ),
    ANSWER_HEADER_NOT_EXIST(546,"当前回答的表头数据不存在" ),
    ANSWER_DATA_NO_EXIST(547,"当前回答内容不存在" ),
    PROJECT_HAS_TEXT(548,"项目是文本回答类型,不能进行此操作" ),
    PROJECT_NO_TEXT(549,"项目是选择类型,不能进行此类操作" ),
    CHOOSE_USER_TO_ADD(550,"请选择需要添加的用户" ),
    ADD_ADMIN_UNCHECK_ERROR(551,"项目不能添加管理员或未审核用户" ),
    CONTENT_CAN_NOT_NULL(552,"回答的内容不能为空" ),
    RELEASE_PACKAGE_STATUS_ERROR(553,"只能释放未完成的数据包状态" ),
    PROJECT_CAN_NOT_DELETE(554,"只有未启动或已完成的项目才能进行删除" ),
    HAS_WRONG_ANSWER_TO_CHANGE(555,"有错误答案需有修改" ),
    PROJECT_UN_FINISH_CAN_NOT_DELETE_USER(556,"当前用户有未完成的项目,无法删除当前用户" );

    private Integer code;
    private String msg;

}
