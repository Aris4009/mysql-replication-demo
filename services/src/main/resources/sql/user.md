sample
===

* 注释

  select #{use("cols")} from user where #{use("condition")}

cols
===

	id,name

updateSample
===

	id=#{id},name=#{name}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(name)){
	 and name=#{name}
	-- @}

list
===

    select * from user where #{use("condition")} 