package org.example.dynamicProxy;

public class NullableAccountDAOProxy implements Nullable, AccountDAO {
    private boolean enabled;
    private AccountDAO accountDAO;

    public void NullableProxy(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void accountByEmail(String email) {
        if (!isEnabled() && email == null) {
            throw new IllegalArgumentException("argument of accountByEmail cannot be null");
        }
        accountDAO.accountByEmail(email);
    }
}