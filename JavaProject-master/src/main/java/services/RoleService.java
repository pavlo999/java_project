package services;

import models.Role;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class RoleService {
    private final Session context;

    public RoleService(Session context) {
        this.context = context;
    }

    public Role GetById(int id) {
        Role role = context.get(Role.class, id);
        return role;
    }

    public void CreateRole(Role model) {
        try {
            Role role = new Role();
            role.setName(model.getName());
            context.save(role);
            context.beginTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Create role error: " + ex.getMessage());
        }
    }

    public List<Role> GetAllRoles() {
        try {
            Query query = context.createQuery("FROM Role");
            List<Role> list = query.list();
            return list;
        } catch (Exception ex) {
            System.out.println("Get roles error: " + ex.getMessage());
            return null;
        }
    }

    public void UpdateRole(Role model) {
        try {
            context.update(model);
            context.beginTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Update role error: " + ex.getMessage());
        }

    }

    public void DeleteRole(Role model) {
        try {
            context.delete(model);
            context.beginTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Delete role error: " + ex.getMessage());
        }
    }

    public void DeleteRole(int id) {
        Role role = this.GetById(id);
        if (role != null) {
            this.DeleteRole(role);
        } else {
            System.out.println("Role not found");
        }
    }
}
