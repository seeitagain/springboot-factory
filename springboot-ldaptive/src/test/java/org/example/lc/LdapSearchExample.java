package org.example.lc;


import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class LdapSearchExample {
    public static void main(String[] args) {
        String ldapUrl = "ldap://localhost:1389";
        String baseDn = "dc=northking,dc=net";
        String userDn = "cn=admin,dc=northking,dc=net";
        String password = "123456";
        String filter = "(uid=jdoe)"; // 示例过滤器

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            DirContext ctx = new InitialDirContext(env);
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> results = ctx.search(baseDn, filter, searchControls);

            while (results.hasMore()) {
                SearchResult result = results.next();
                System.out.println(result.getNameInNamespace());
            }

            ctx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
