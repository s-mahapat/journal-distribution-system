select * from subscription t1 where not exists(select t2.subscriptionID from subscriptiondetails t2 where t1.id = t2.subscriptionID)