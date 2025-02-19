select * from friendship;

select f.*
from friendship as f
         join friendship as sf
              on f.to_member_id = sf.from_member_id
where f.from_member_id = UUID_TO_BIN('8de3e1ff-1007-44ba-914a-30a67131746d')
  and f.from_member_id = sf.to_member_id
  and f.to_member_id = sf.from_member_id
  and f.is_friend = true
  and sf.is_friend = true;


select * from member as m
where m.id in (
               UUID_TO_BIN('1fa5ec70-ffec-4b35-812e-180346bb846e'),
               UUID_TO_BIN('65d6cf51-1c6b-4d43-9213-beec70a31616'),
               UUID_TO_BIN('6b2a24b4-8268-41c2-b75c-923eb2551714'),
               UUID_TO_BIN('789aab3f-b7e5-45fd-bafe-3f2778eb43e2'),
               UUID_TO_BIN('b017a297-7e50-411b-877f-ce63fae98dce'),
               UUID_TO_BIN('de02fbd0-39fc-4c26-9177-f94c5965243a'),
               UUID_TO_BIN('1bad34be-1d64-444b-92ff-f4197e62f70e')
    );
