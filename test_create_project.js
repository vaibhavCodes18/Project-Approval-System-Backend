const http = require('http');

const postData = JSON.stringify({
    email: 'vaibhav@gmail.com',
    password: 'password123'
});

const options = {
    hostname: 'localhost',
    port: 8080,
    path: '/api/v1/auth/login',
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(postData)
    }
};

const req = http.request(options, (res) => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => {
        try {
            const parsed = JSON.parse(data);
            const token = parsed.data.accessToken;
            if (!token) throw new Error('No token');
            
            // Now create project
            const createData = JSON.stringify({
                title: "Smart Hire AI " + Date.now(), // unique title!
                description: "This is a ai tool to shortlist the candidate.",
                guideId: "69dbf22155f41446a3e7ff9b"
            });
            
            const req2 = http.request({
                hostname: 'localhost',
                port: 8080,
                path: '/api/v1/projects',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                    'Content-Length': Buffer.byteLength(createData)
                }
            }, (res2) => {
                let d2 = '';
                res2.on('data', chunk => d2 += chunk);
                res2.on('end', () => console.log('Create Res:', d2));
            });
            req2.write(createData);
            req2.end();
            
        } catch (e) {
            console.log('Login failed:', data);
        }
    });
});

req.on('error', (e) => console.error(e));
req.write(postData);
req.end();
