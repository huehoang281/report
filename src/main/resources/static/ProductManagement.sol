// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract ProductManagement {
    struct Product {
        string name;
        uint256 price;
        uint256 quantity;
    }

    Product[] public products;

    event ProductAdded(string name, uint256 price, uint256 quantity);
    event ProductUpdated(string newName, uint256 newPrice, uint256 newQuantity);
    event ProductDeleted();
    event ProductBoughtSold(uint256 quantity);

    function addProduct(string memory _name, uint256 _price, uint256 _quantity) external {
        Product memory newProduct = Product({
            name: _name,
            price: _price,
            quantity: _quantity
        });
        products.push(newProduct);

        emit ProductAdded(_name, _price, _quantity);
    }

    function displayProduct(uint256 _index) external view returns (string memory, uint256, uint256) {
        require(_index < products.length, "Invalid index");
        Product storage product = products[_index];
        return (product.name, product.price, product.quantity);
    }

    function updateProduct(uint256 _index, string memory _newName, uint256 _newPrice, uint256 _newQuantity) external {
        require(_index < products.length, "Invalid index");
        Product storage product = products[_index];
        product.name = _newName;
        product.price = _newPrice;
        product.quantity = _newQuantity;

        emit ProductUpdated(_newName, _newPrice, _newQuantity);
    }

    function deleteProduct(uint256 _index) external {
        require(_index < products.length, "Invalid index");
        products[_index] = products[products.length - 1];
        products.pop();

        emit ProductDeleted();
    }

    function buySellProduct(uint256 _index, uint256 _quantity) external {
        require(_index < products.length, "Invalid index");
        Product storage product = products[_index];
        require(product.quantity >= _quantity, "Not enough quantity to buy");
        product.quantity -= _quantity;

        emit ProductBoughtSold(_quantity);
    }
}
