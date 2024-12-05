class ApiClient {
    constructor(baseUrl = 'http://localhost:8080') {
        this.baseUrl = baseUrl;
    }

    async fetchPublicData() {
        try {
            const response = await fetch(`${this.baseUrl}/api/public/data`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return await this.handleResponse(response);
        } catch (error) {
            this.handleError(error);
        }
    }

    async fetchProtectedData(token) {
        try {
            const response = await fetch(`${this.baseUrl}/api/protected/data`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            return await this.handleResponse(response);
        } catch (error) {
            this.handleError(error);
        }
    }

    async updateData(token, data) {
        try {
            const response = await fetch(`${this.baseUrl}/api/protected/update`, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(data)
            });
            return await this.handleResponse(response);
        } catch (error) {
            this.handleError(error);
        }
    }

    async handleResponse(response) {
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.message || 'API request failed');
        }
        return data;
    }

    handleError(error) {
        console.error('API Error:', error);
        throw error;
    }
}

// UI Controller
class UIController {
    constructor() {
        this.apiClient = new ApiClient();
        this.setupEventListeners();
        this.token = localStorage.getItem('token') || '';
    }

    setupEventListeners() {
        document.getElementById('fetchPublic').addEventListener('click', () => this.fetchPublicData());
        document.getElementById('fetchProtected').addEventListener('click', () => this.fetchProtectedData());
        document.getElementById('updateData').addEventListener('click', () => this.updateData());
        document.getElementById('token').addEventListener('input', (e) => this.updateToken(e));
    }

    updateToken(event) {
        this.token = event.target.value;
        localStorage.setItem('token', this.token);
    }

    showLoading(elementId) {
        const element = document.getElementById(elementId);
        element.innerHTML = '<div class="loading"></div>';
    }

    showResponse(elementId, data, isError = false) {
        const element = document.getElementById(elementId);
        const statusClass = isError ? 'error' : 'success';
        const statusDot = `<span class="status-indicator status-${statusClass}"></span>`;
        element.innerHTML = `
            <div class="response ${statusClass}">
                ${statusDot}
                <pre>${JSON.stringify(data, null, 2)}</pre>
            </div>
        `;
    }

    async fetchPublicData() {
        try {
            this.showLoading('publicResponse');
            const data = await this.apiClient.fetchPublicData();
            this.showResponse('publicResponse', data);
        } catch (error) {
            this.showResponse('publicResponse', { error: error.message }, true);
        }
    }

    async fetchProtectedData() {
        try {
            if (!this.token) {
                throw new Error('Token is required for protected endpoints');
            }
            this.showLoading('protectedResponse');
            const data = await this.apiClient.fetchProtectedData(this.token);
            this.showResponse('protectedResponse', data);
        } catch (error) {
            this.showResponse('protectedResponse', { error: error.message }, true);
        }
    }

    async updateData() {
        try {
            if (!this.token) {
                throw new Error('Token is required for protected endpoints');
            }
            this.showLoading('updateResponse');
            const data = {
                message: 'Test update data',
                timestamp: new Date().toISOString()
            };
            const response = await this.apiClient.updateData(this.token, data);
            this.showResponse('updateResponse', response);
        } catch (error) {
            this.showResponse('updateResponse', { error: error.message }, true);
        }
    }
}

// Initialize UI when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new UIController();
});
