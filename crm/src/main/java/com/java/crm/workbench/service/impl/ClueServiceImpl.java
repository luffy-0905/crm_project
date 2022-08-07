package com.java.crm.workbench.service.impl;

import com.java.crm.commens.contants.Contants;
import com.java.crm.commens.utils.DateUtils;
import com.java.crm.commens.utils.UUIDUtils;
import com.java.crm.settings.domain.User;
import com.java.crm.workbench.domain.*;
import com.java.crm.workbench.mapper.*;
import com.java.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;


    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue queryClueDetailById(String id) {
        return clueMapper.selectClueDetailById(id);
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        //这里不通过session获取user，因为session是web技术，他的运行离不开服务器，如果在service层引用session,他就不能单独运行.(一般属于web技术的类不会传到service层)
        String clueId= (String) map.get("clueId");
        String isCreateTran=(String) map.get("isCreateTran");
        User user=(User)map.get(Contants.SESSION_USER);
        //根据id查询线索信息
        Clue clue = clueMapper.selectClueById(clueId);
        //把线索中有关公司的信息转换的客户表中
        Customer c=new Customer();
        c.setAddress(clue.getAddress());
        c.setContactSummary(clue.getContactSummary());
        c.setCreateBy(user.getId());
        c.setCreateTime(DateUtils.formatDateTime(new Date()));
        c.setDescription(clue.getDescription());
        c.setId(UUIDUtils.getUUID());
        c.setName(clue.getCompany());
        c.setNextContactTime(clue.getNextContactTime());
        c.setOwner(user.getId());
        c.setPhone(clue.getPhone());
        c.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(c);
        //把线索中的有关个人的信息转换到联系人表中
        Contacts con=new Contacts();
        con.setAddress(clue.getAddress());
        con.setAppellation(clue.getAppellation());
        con.setContactSummary(clue.getContactSummary());
        con.setCreateBy(user.getId());
        con.setCreateTime(DateUtils.formatDateTime(new Date()));
        con.setCustomerId(c.getId());
        con.setDescription(clue.getDescription());
        con.setEmail(clue.getEmail());
        con.setFullname(clue.getFullname());
        con.setId(UUIDUtils.getUUID());
        con.setJob(clue.getJob());
        con.setMphone(clue.getMphone());
        con.setNextContactTime(clue.getNextContactTime());
        con.setOwner(user.getId());
        con.setSource(clue.getSource());
        contactsMapper.insertContacts(con);
        //根据clueId查询线索下所有的备注
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //把该线索下的所有备注转换到客户备注表中和联系人备注表中
        if(clueRemarkList!=null && clueRemarkList.size()>0){
            //遍历list
            CustomerRemark cur=null;
            ContactsRemark cor=null;
            List<CustomerRemark> curList=new ArrayList<>();
            List<ContactsRemark> conList=new ArrayList<>();
            for(ClueRemark cr:clueRemarkList){
                cur = new CustomerRemark();
                cur.setCreateBy(cr.getCreateBy());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCustomerId(c.getId());
                cur.setEditBy(cr.getEditBy());
                cur.setEditFlag(cr.getEditFlag());
                cur.setEditTime(cr.getEditTime());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                curList.add(cur);

                cor=new ContactsRemark();
                cor.setContactsId(con.getId());
                cor.setCreateBy(cr.getCreateBy());
                cor.setCreateTime(cr.getCreateTime());
                cor.setEditBy(cr.getEditBy());
                cor.setEditFlag(cr.getEditFlag());
                cor.setEditTime(cr.getEditTime());
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                conList.add(cor);
            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(conList);
        }
        //根据clueId查询线索与市场活动的关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        //遍历carList
        if(carList!=null && carList.size()>0){
            ContactsActivityRelation contactsActivityRelation=null;
            List<ContactsActivityRelation> contactsActivityRelationList=new ArrayList<>();
            for(ClueActivityRelation car:carList){
                contactsActivityRelation=new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(car.getActivityId());
                contactsActivityRelation.setContactsId(con.getId());
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            //保存联系人与活动关系表
            contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);
        }
        //判断是否需要添加交易，还需要添加线索备注到交易备注
        if("true".equals(isCreateTran)){
            Tran tran=new Tran();
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(con.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formatDateTime(new Date()));
            tran.setCustomerId(c.getId());
            tran.setExpectedDate((String)map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String)map.get("money"));
            tran.setName((String)map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String)map.get("stage"));
            //保存交易
            tranMapper.insertTran(tran);

            if(clueRemarkList!=null && clueRemarkList.size()>0){
                TranRemark tranRemark=null;
                List<TranRemark> tranRemarkList=new ArrayList<>();
                for (ClueRemark cr:clueRemarkList){
                    tranRemark=new TranRemark();
                    tranRemark.setCreateBy(cr.getCreateBy());
                    tranRemark.setCreateTime(cr.getCreateTime());
                    tranRemark.setEditBy(cr.getEditBy());
                    tranRemark.setEditFlag(cr.getEditFlag());
                    tranRemark.setEditTime(cr.getEditTime());
                    tranRemark.setId(UUIDUtils.getUUID());
                    tranRemark.setNoteContent(cr.getNoteContent());
                    tranRemark.setTranId(tran.getId());

                    tranRemarkList.add(tranRemark);
                }
                //保存交易备注
                tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
            }
        }
        //删除线索备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //删除线索与市场活动的关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //删除线索
        clueMapper.deleteClueById(clueId);
    }
}
