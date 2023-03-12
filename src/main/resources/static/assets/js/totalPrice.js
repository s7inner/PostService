const shipmentPrice = document.getElementById('shipmentPrice');
const shipmentPriceTotal = document.getElementById('shipmentPriceTotal');

shipmentPrice.addEventListener('input', () => {
    const price = parseFloat(shipmentPrice.value);
    if (!isNaN(price)) {
        const totalPrice = price * 1.1;
        shipmentPriceTotal.value = totalPrice.toFixed(2);
    } else {
        shipmentPriceTotal.value = '';
    }
});