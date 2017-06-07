/**
如果该对象已经改变(changed来指示)，
那么通知观察者，并在之前调用clearChanged()指示此对象已不再更改。
@param arg 任何对象
*/
Observable.prototype.notifyObservers = function(arg) {
	if (!this.changed)
		return;
	this.clearChanged();
	var self = this;
	this.obs.forEach(function(ob){
		ob.update(self, arg);
	});
};
/**
标记这个被观察者对象已经改变
*/
Observable.prototype.setChanged = function() {
    this.changed = true;
    return this;
};
/**
指示这个对象已不再更改，或者已通知所有观察者其最近的变化
*/
Observable.prototype.clearChanged = function() {
    this.changed = false;
};

/**
@class 观察者
@author hulang
@date 2017-04-25
*/
function Observer() {
}
/**
更新，该方法需要被覆写
*/
Observer.prototype.update = function() {};
