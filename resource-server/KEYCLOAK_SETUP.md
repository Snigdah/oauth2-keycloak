# Keycloak setup for Product resource server

Realm: **OneBank**

## 1. Client (Resource Server)

- **Client ID:** `Product`
- **Client authentication:** ON (confidential)
- **Client secret:** (copy from Keycloak → Clients → Product → Credentials)
- **Valid redirect URIs:** (as needed for login; resource server may leave empty)
- **Authorization:** Enabled (for Authorization Services)

## 2. Client scopes (for Product client)

Create scopes and assign to client:

| Scope name   | Description   |
|-------------|----------------|
| `product:create` | Create product (Manager) |
| `product:read`   | Read product (Supervisor) |

**Steps:**  
Clients → Product → Client scopes → Add client scope → create `product:create` and `product:read` (or add as optional/default client scopes so they appear in the access token).

Alternatively use **Client scopes** (realm): create two client scopes `product:create` and `product:read`, then assign them to client **Product** (e.g. as optional and add to token via mapper or role).

## 3. Resources and URIs (Authorization tab)

Clients → Product → Authorization → Resources.

**Resource:** Product (or one resource per path, as below)

| Resource name | URIs                    | Scopes           |
|---------------|-------------------------|------------------|
| Product       | `/product/create/today`, `/product/read/history` | `product:create`, `product:read` |

Or two resources:

| Resource name     | URIs                 | Scopes         |
|-------------------|----------------------|----------------|
| Product Create    | `/product/create/today`  | `product:create` |
| Product Read      | `/product/read/history`  | `product:read`   |

**Scopes** (Authorization → Authorization scopes): ensure `product:create` and `product:read` exist (often created with the resource).

## 4. Roles

Realm roles (or client roles for Product):

| Role        | Purpose                          |
|-------------|----------------------------------|
| **Manager**   | Access `/product/create/today` (scope `product:create`) |
| **Supervisor** | Access `/product/read/history` (scope `product:read`)   |

## 5. Policies (Authorization tab)

Clients → Product → Authorization → Policies.

- **Policy: Manager-only**  
  Type: Role → Realm roles (or Client roles) → select **Manager**.
- **Policy: Supervisor-only**  
  Type: Role → Realm roles (or Client roles) → select **Supervisor**.

## 6. Permissions (scope-based)

Authorization → Permissions.

- **Permission for product:create**  
  Resource: (resource that has URI `/product/create/today`), Scopes: `product:create`, Policies: **Manager-only**.
- **Permission for product:read**  
  Resource: (resource that has URI `/product/read/history`), Scopes: `product:read`, Policies: **Supervisor-only**.

So:

- Manager gets scope `product:create` in token → can call GET `/product/create/today`.
- Supervisor gets scope `product:read` in token → can call GET `/product/read/history`.

## 7. Scope in token (role → scope mapping)

To have `product:create` / `product:read` in the **access token** for Manager/Supervisor:

- Either use **Client scope** mappers that add these scopes when the user has role Manager/Supervisor.
- Or use **Client roles** on Product: create client roles `product:create` and `product:read`, assign Manager → `product:create`, Supervisor → `product:read`, and add a **Client scope** (or protocol mapper) so that Product client roles are mapped to scopes in the access token (e.g. audience + scope claim).

**Simple approach:**  
Clients → Product → Client scopes → add client scopes that add roles as scopes (e.g. “roles” or a custom scope mapper that maps client role `product:create` to scope `product:create`). Assign Manager the client role that maps to `product:create`, Supervisor the one that maps to `product:read`.

## 8. Users

- Create user **manager1** → assign realm role **Manager** (and client role for Product if using client roles).
- Create user **supervisor1** → assign realm role **Supervisor**.

Obtain tokens via Keycloak (e.g. token endpoint or “Test” in client) and call:

- As Manager: `GET http://localhost:8085/product/create/today` with Bearer token.
- As Supervisor: `GET http://localhost:8085/product/read/history` with Bearer token.
