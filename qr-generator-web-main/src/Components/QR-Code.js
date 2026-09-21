import React, { useState, useEffect } from 'react';
import QRCode from 'qrcode.react';
import axios from 'axios';

var cors = require('cors')
app.use(cors())

const QRCodeGenerator = () => {
    const [qrText, setQRText] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            const result = await axios.get('http://localhost:8080/authorize');
            console.log(result);
            setQRText(result.data.qrValue);
        }
        fetchData();
    }, []);

    return (
        <div>
            <QRCode value={qrText} />
        </div>
    );
}

export default QRCodeGenerator;



