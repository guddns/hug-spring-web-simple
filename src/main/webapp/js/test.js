var Test = function() {
	this.a = 0;
	this.b = 3;
}

Test.prototype.test = function() {
	console.log(this.a);
}