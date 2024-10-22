package org.example.lc.service;

import org.example.lc.pojo.request.AddUserReq;
import org.example.lc.pojo.request.DelUserReq;
import org.example.lc.pojo.response.ResponseBean;
import org.ldaptive.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LdapService {
    @Value("${ldap.base-dn}")
    private String baseDn;

    @Autowired
    private ConnectionFactory connectionFactory;

    public SearchResult search(String filter) throws LdapException {
        Connection conn = connectionFactory.getConnection();
        try {
            conn.open();

            SearchOperation search = new SearchOperation(conn);
            SearchResult result = search.execute(
                    new SearchRequest(
                            baseDn, filter)).getResult();

            for (LdapEntry entry : result.getEntries()) {
                // do something useful with the entry
                System.out.println(entry.getDn());
                for(LdapAttribute attribute : entry.getAttributes()){
                    System.out.println(attribute.getName() + "------" + attribute.getStringValue());
                }
            }
        } finally {
            conn.close();
        }
        return null;
    }

    public ResponseBean<Boolean> addUser(AddUserReq req) throws LdapException {
        LdapEntry entry = new LdapEntry(
                "cn=" + req.getUserName() + ",ou=users,dc=northking,dc=net",
                new LdapAttribute("objectClass", "inetOrgPerson"),
                new LdapAttribute("cn", req.getUserName()),
                new LdapAttribute("sn", req.getUserName()),
                new LdapAttribute("userPassword", req.getPwd()),
                new LdapAttribute("mail", req.getEmail()));

        Connection conn = connectionFactory.getConnection();
        try {
            conn.open();
            AddOperation add = new AddOperation(conn);
            add.execute(new AddRequest(entry.getDn(), entry.getAttributes()));
        } finally {
            conn.close();
        }
        return null;
    }

    public ResponseBean<Boolean> del(DelUserReq req) throws LdapException {
        Connection conn = connectionFactory.getConnection();
        try {
            conn.open();
            DeleteOperation delete = new DeleteOperation(conn);
            delete.execute(new DeleteRequest("cn=" + req.getUserName() + ",ou=users,dc=northking,dc=net"));
        } finally {
            conn.close();
        }
        return null;
    }
}